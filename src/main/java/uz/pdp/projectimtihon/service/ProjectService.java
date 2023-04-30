package uz.pdp.projectimtihon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.projectimtihon.entity.Project;
import uz.pdp.projectimtihon.exeptions.RestException;
import uz.pdp.projectimtihon.payload.ApiResponse;
import uz.pdp.projectimtihon.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ApiResponse<String> editProjectName(String name, Integer id) {
        Optional<Project> byId = projectRepository.findById(id);
        if(!byId.isPresent()){
            throw RestException.restThrow("Project not found");
        }
        Project project = byId.get();
        if(name.length()<3 || name.length()>256){
            throw RestException.restThrow("Project name not available");
        }
        project.setName(name);
        projectRepository.save(project);
        return ApiResponse.successResponse("OK");
    }

    public ApiResponse<List<Project>> getAllProjects(Integer userId) {
        List<Project> projectList = projectRepository.findByUserId(userId);
        return ApiResponse.successResponse(projectList);
    }

    public ApiResponse<String> deleteProject(Integer id) {
        try {
            projectRepository.deleteById(id);
            return ApiResponse.successResponse("OK");
        } catch (Exception e) {
            throw RestException.restThrow("ERROR");
        }
    }
}
