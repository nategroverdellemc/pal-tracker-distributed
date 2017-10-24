package io.pivotal.pal.tracker.backlog;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Hashtable;
import java.util.Map;

public class ProjectClient {
    private final RestOperations restOperations;
    private final String endpoint;

    private Map<Long,ProjectInfo> cachedProjects = new Hashtable<Long,ProjectInfo>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo foundProject =  restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        if(foundProject != null){
            cachedProjects.put(projectId,foundProject);
        }
        return foundProject;
    }

    public ProjectInfo getProjectFromCache(Long projectId){
        return cachedProjects.get(projectId);
    }
}
