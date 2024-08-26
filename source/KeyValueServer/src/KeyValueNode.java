import java.util.List;
import java.util.ArrayList;

public class KeyValueNode{

    public String key;
    public List<KeyValueNode> keyValues = new ArrayList<>();

    public String value;

    public KeyValueNode(){

    }

    public KeyValueNode(String key, KeyValueNode kV){
        this.key = key;
        keyValues.add(kV);
    }

    public KeyValueNode(String key){
        this.key = key;
    }

    public KeyValueNode(String key, String value){
        this.key = key;
        this.value = value;
    }

}