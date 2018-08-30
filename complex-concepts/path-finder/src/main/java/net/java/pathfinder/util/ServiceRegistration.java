package net.java.pathfinder.util;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.*;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;
import net.java.discovery.ServiceAdvertisement;
import net.java.discovery.ServiceAdvertizer;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class ServiceRegistration {
    
    @Resource
    private ManagedScheduledExecutorService scheduler;
    
    private ServiceAdvertisement advert;
    
    private ServiceAdvertizer advertizer;
    
    @PostConstruct
    public void init() {
        advertizer = new ServiceAdvertizer();
    }

    public void registerAtStartup(@Observes @Initialized(ApplicationScoped.class) ServletContext context) {
        advert = new ServiceAdvertisement()
                .withName("pathfinder")
                .withContextRoot(context.getContextPath());
        advertizer.advertize(advert, scheduler);
        
        scheduler.scheduleAtFixedRate(() -> {
            Logger.getLogger(this.getClass().getName()).info(() -> {
                String url = ConfigProvider.getConfig().getValue("discovery.service.pathfinder.url", String.class);
                return "URL of a healthy pathfinder service: " + url;
            });
        }, 1, 5, TimeUnit.SECONDS);
    }

    public void deregisterAtShutdown(@Observes @BeforeDestroyed(ApplicationScoped.class) Object destroy) {
        advertizer.stopAdvertizing(advert);
    }
}
