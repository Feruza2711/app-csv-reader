package uz.pdp.projectimtihon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.projectimtihon.entity.Project;
import uz.pdp.projectimtihon.entity.Section;
import uz.pdp.projectimtihon.entity.Task;
import uz.pdp.projectimtihon.entity.User;
import uz.pdp.projectimtihon.exeptions.RestException;
import uz.pdp.projectimtihon.payload.ApiResponse;
import uz.pdp.projectimtihon.repository.ProjectRepository;
import uz.pdp.projectimtihon.repository.SectionRepository;
import uz.pdp.projectimtihon.repository.TaskRepository;
import uz.pdp.projectimtihon.repository.UserRepository;

import java.io.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaveCSVFileService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;

    public ApiResponse<String> SaveCSVFile(MultipartFile saveFile, Integer userId) {
        String line = "";
        try {
            InputStream inputStream = saveFile.getInputStream();
            InputStreamReader reader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");

                Project project=new Project();
                project.setName(saveFile.getName());
                Optional<User> byId = userRepository.findById(userId);
                if(byId.isEmpty())
                    throw RestException.restThrow("User not found");
                User user = byId.get();
                project.setUser(user);
                projectRepository.save(project);

                try {
                    Task task = new Task();
                    task.setTaskNumber(Integer.valueOf(data[0]));

                    Section section = new Section();
                    String sectionName = data[1];

                    Optional<Section> sectionOptional = sectionRepository.findByName(sectionName);
                    if (!sectionOptional.isEmpty()) {
                        section.setName(sectionName);
                        sectionRepository.save(section);
                    }

                    task.setSection(sectionOptional.get());
                    task.setName(data[2]);
                    task.setDescription(data[3]);

                    String begin_date=data[4];
                    String[] beginDate = begin_date.split("/");
                    int year = Integer.parseInt(beginDate[2]);
                    int month = Integer.parseInt(beginDate[0]);
                    int day = Integer.parseInt(beginDate[1]);
                    LocalDate begin_date_db = LocalDate.of(year, month, day);

                    String end_date=data[5];
                    String[] endDate = end_date.split("/");
                    int year1 = Integer.parseInt(endDate[2]);
                    int month1 = Integer.parseInt(endDate[0]);
                    int day1 = Integer.parseInt(endDate[1]);
                    LocalDate end_date_db = LocalDate.of(year1, month1, day1);

                    if((begin_date.isEmpty() && !end_date.isEmpty()) || (! begin_date.isEmpty() && end_date.isEmpty())){
                        throw RestException.restThrow("Dates is not available");
                    }
                    if(!begin_date_db.isBefore(end_date_db)){
                        throw RestException.restThrow("Dates is not available");
                    }
                    task.setBeginDate(begin_date_db);
                    task.setEndDate(end_date_db);

                    task.setAssignee(data[6]);
                    String dependency= data[7];
                    String dependency1 = dependency.substring(1, dependency.length() - 1);
                    task.setDependency(dependency1);
                    taskRepository.save(task);

                } catch (NumberFormatException e) {
                    throw RestException.restThrow("Error");
                }
            }
            return ApiResponse.successResponse("OK");


        } catch (IOException e) {
            throw RestException.restThrow("File is not available");
        }
    }
}


