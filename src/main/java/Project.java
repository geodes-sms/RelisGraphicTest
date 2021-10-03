import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {


    private String project_name;

    private RelisUser project_owner;

    private ArrayList<RelisUser> reviewer = new ArrayList<>();

    public boolean isProjectFor(RelisUser relisUser){
        return project_owner != null && relisUser.equals(project_owner);

    }


}
