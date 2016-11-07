package net.java.cargotracker.infrastructure.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import net.java.cargotracker.application.util.JacksonConfigurationContextResolver;
import net.java.cargotracker.domain.model.cargo.Itinerary;
import net.java.cargotracker.domain.model.cargo.Leg;
import net.java.cargotracker.domain.model.cargo.RouteSpecification;
import net.java.cargotracker.domain.model.location.LocationRepository;
import net.java.cargotracker.domain.model.location.UnLocode;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;
import net.java.cargotracker.domain.model.voyage.VoyageRepository;
import net.java.cargotracker.domain.service.RoutingService;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * Our end of the routing service. This is basically a data model translation
 * layer between our domain model and the API put forward by the routing team,
 * which operates in a different context from us.
 *
 */
@Stateless
public class ExternalRoutingService implements RoutingService {

    // TODO Use injection instead?
    private static final Logger LOGGER = Logger.getLogger(
            ExternalRoutingService.class.getName());

    @Resource(name = "pathFinderDiscoveryUrl")
    private String pathFinderDiscoveryUrl;

    // TODO Can I use injection?
    private final Client jaxrsClient = ClientBuilder.newClient();
    private WebTarget graphTraversalResource;
    @Inject
    private LocationRepository locationRepository;
    @Inject
    private VoyageRepository voyageRepository;

    @PostConstruct
    public void init() {
        WebTarget pathFinderDiscoveryResource = jaxrsClient.target(pathFinderDiscoveryUrl);

        JsonArray discoveryData = pathFinderDiscoveryResource
                .request(MediaType.APPLICATION_JSON).get(JsonArray.class);
        String address = discoveryData.getJsonObject(0).getString("ServiceAddress");
        // String address = "172.17.0.1";
        int port = discoveryData.getJsonObject(0).getInt("ServicePort");

        graphTraversalResource = jaxrsClient.target(
                "http://" + address + ":" + port + "/rest/graph-traversal/shortest-path");
        graphTraversalResource.register(JacksonConfigurationContextResolver.class);
        graphTraversalResource.register(JacksonFeature.class);
    }

    @Override
    public List<Itinerary> fetchRoutesForSpecification(
            RouteSpecification routeSpecification) {
        // The RouteSpecification is picked apart and adapted to the external API.
        String origin = routeSpecification.getOrigin().getUnLocode().getIdString();
        String destination = routeSpecification.getDestination().getUnLocode()
                .getIdString();

        List<TransitPath> transitPaths = graphTraversalResource
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<TransitPath>>() {
                });

        // The returned result is then translated back into our domain model.
        List<Itinerary> itineraries = new ArrayList<>();

        for (TransitPath transitPath : transitPaths) {
            Itinerary itinerary = toItinerary(transitPath);
            // Use the specification to safe-guard against invalid itineraries
            if (routeSpecification.isSatisfiedBy(itinerary)) {
                itineraries.add(itinerary);
            } else {
                LOGGER.log(Level.FINE,
                        "Received itinerary that did not satisfy the route specification");
            }
        }

        return itineraries;
    }

    private Itinerary toItinerary(TransitPath transitPath) {
        List<Leg> legs = new ArrayList<>(transitPath.getTransitEdges().size());
        for (TransitEdge edge : transitPath.getTransitEdges()) {
            legs.add(toLeg(edge));
        }
        return new Itinerary(legs);
    }

    private Leg toLeg(TransitEdge edge) {
        return new Leg(
                voyageRepository.find(new VoyageNumber(edge.getVoyageNumber())),
                locationRepository.find(new UnLocode(edge.getFromUnLocode())),
                locationRepository.find(new UnLocode(edge.getToUnLocode())),
                edge.getFromDate(), edge.getToDate());
    }
}
