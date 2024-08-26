import java.util.List;
import java.util.ArrayList;

public class Trie {

    TrieNode[] branches = new TrieNode[10];
    public List<TrieNode> branchList = new ArrayList<>();

    public String Add(KeyValueNode kV) throws Exception{

        String key = kV.key.substring(1, kV.key.length() - 1);
        List<TrieNode> branches = branchList;
        boolean foundNode = false;

        for (int i = 0; i < key.length(); i++){
            String crntChar = Character.toString(key.charAt(i));
            //System.out.println("crntChar = " + crntChar);
            foundNode = false;
            for (int j = 0; j < branches.size(); j++){
                //System.out.println("branches.get(j).str = " + branches.get(j).str);
                if (branches.get(j).str.equals(crntChar)){
                    //System.out.println("I AM IN");
                    foundNode = true;
                    if (i == key.length() - 1){
                        //System.out.println("end");
                        branches.get(j).kV = kV;
                        return kV.key + ": Done";
                    }
                    branches = branches.get(j).branchList;
                    break;
                }
            }

            if (!foundNode){
                TrieNode nT = new TrieNode(crntChar);
                branches.add(nT);
                branches = nT.branchList;
                if (i == key.length() - 1){
                    //System.out.println("end");
                    nT.kV = kV;
                    return kV.key + ": Done";
                }
            }


        }

        return kV.key + ": Done";
    }

    public KeyValueNode Find(String fullKey) throws Exception{
        KeyValueNode foundKV = null;
        String key = fullKey.substring(1, fullKey.length() - 1);
        List<TrieNode> branches = branchList;
        boolean foundNode = false;
        for (int i = 0; i < key.length(); i++){
            String crntChar = Character.toString(key.charAt(i));;
            foundNode = false;
            for (int j = 0; j < branches.size(); j++){
                //System.out.println(branches.get(j).str);
                if (branches.get(j).str.equals(crntChar)){
                    if (i == key.length() - 1){
                        if (branches.get(j).kV != null){
                            foundKV = branches.get(j).kV;
                            return foundKV;
                        }
                        else {
                            throw new Exception(fullKey + ": Key not found"); 
                        }
                    }
                    branches = branches.get(j).branchList;
                    foundNode = true;
                    break;
                }
            }

            if (!foundNode){
                throw new Exception(fullKey + ": Key not found");
            }
        }
        
        return foundKV;
    }

    public String Delete(String fullKey) throws Exception{
        String key = fullKey.substring(1, fullKey.length() - 1);
        List<TrieNode> branches = branchList;
        boolean foundNode = false;
        for (int i = 0; i < key.length(); i++){
            String crntChar = Character.toString(key.charAt(i));;
            foundNode = false;
            for (int j = 0; j < branches.size(); j++){
                //System.out.println(branches.get(j).str);
                if (branches.get(j).str.equals(crntChar)){
                    if (i == key.length() - 1){
                        branches.get(j).kV = null;
                        return "Deleted: " + fullKey;
                    }
                    branches = branches.get(j).branchList;
                    foundNode = true;
                    break;
                }
            }

            if (!foundNode){
                throw new Exception(fullKey + ": Could not get deleted");
            }
        }

        return "ERROR";
    }
}
