FROM glassfish
COPY cargo-tracker.war /
RUN /usr/local/glassfish4/bin/asadmin start-domain && /usr/local/glassfish4/bin/asadmin -u admin deploy /cargo-tracker.war && /usr/local/glassfish4/bin/asadmin stop-domain
EXPOSE 8080
CMD asadmin start-domain --verbose