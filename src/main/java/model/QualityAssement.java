package model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class QualityAssement {


    private String name ="Classification";

    private int score = 0;

    public QualityAssement(String assets, int score, int dob){
        this.name = assets;
    }
}
