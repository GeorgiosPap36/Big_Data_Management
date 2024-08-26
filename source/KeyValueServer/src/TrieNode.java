import java.util.List;
import java.util.ArrayList;

public class TrieNode{

    public TrieNode[] branches = new TrieNode[10];

    public List<TrieNode> branchList = new ArrayList<>();

    public KeyValueNode kV;
    public String str;

    public TrieNode(String str){
        this.str = str;
    }

    public TrieNode(KeyValueNode kV){
        this.kV = kV;
    }

}