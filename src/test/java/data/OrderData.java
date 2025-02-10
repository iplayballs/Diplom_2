package data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderData {

    private List<String> ingredients;
    private String name;
    private String number;
    private String _id;
}
