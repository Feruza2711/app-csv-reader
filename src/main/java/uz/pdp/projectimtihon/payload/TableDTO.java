package uz.pdp.projectimtihon.payload;

import lombok.Data;
import uz.pdp.projectimtihon.entity.Project;
import uz.pdp.projectimtihon.entity.Section;

import java.util.List;
@Data
public class TableDTO {
    private List<SectionDTO> sectionDTOList;
}
