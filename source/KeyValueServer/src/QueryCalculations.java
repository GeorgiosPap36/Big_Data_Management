

public class QueryCalculations{

    Trie trie = new Trie();

    ///PUT///
    public String Put(String query){
        String outKey = "";
        String outValue = "";
        try{
            outKey = query.split(" -> ")[0];
            outValue = query.split(" -> ", 2)[1];
        }catch(Exception e){
            return "Wrong query format";
        }
        KeyValueNode outterkV;

        if (NumberOfCInS(query, '[') != NumberOfCInS(query, ']'))
            return outKey + ": Brackets are missing"; 

        if (outValue.length() <= 2){
            outterkV = new KeyValueNode(outKey, "");
        }
        else{
            outterkV = new KeyValueNode(outKey);
            outValue = outValue.substring(1, outValue.length() - 1);
            SplitQuery(outterkV, outValue);
        }

        try{
            trie.Add(outterkV);
            return "PUT " + outterkV.key + " is done!";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    private void SplitQuery(KeyValueNode previousKV, String query){

        int orIndex = 0;
        int i = 0;

        while(i < query.length() - 4){

            if (query.substring(i, i + 4).equals(" -> ")){

                String key = query.substring(orIndex, i);
                KeyValueNode newKV = new KeyValueNode(key);
                previousKV.keyValues.add(newKV);

                if (query.charAt(i + 4) == '['){
                    int bracketEnd = FindWhereBracketCloses(query.substring(i + 4)) + i + 4; //FindWhereBracketCloses returns the index of 
                                                                                             //the substring and then I add i + 4 so that the 
                    if (bracketEnd < 0){                                                     //to get the corresponding index of the fulll string
                        System.out.println("Brackets are missing");                       
                        break;
                    }

                    if (bracketEnd - (i+4) >= 3){
                        String kvValue = query.substring(i + 5, bracketEnd - 1);
                        SplitQuery(newKV, kvValue);
                    }
                    else{
                        newKV.value = "";
                    }
                        
                    i = bracketEnd - 1;
                    orIndex = i;
                }
                else {

                    int valueEnd1 = query.indexOf("]", i);
                    int valueEnd2 = query.indexOf(" | ", i);
                    int valueEnd = Math.min(valueEnd1, valueEnd2);

                    if (valueEnd2 <= 0 && valueEnd1 <= 0)
                        valueEnd = query.length();
                    else if (valueEnd1 <= 0)
                        valueEnd = valueEnd2;
                    else if (valueEnd2 <= 0)
                        valueEnd = valueEnd1;


                    String val = query.substring(i + 4, valueEnd);
                    newKV.value = val;

                    i = valueEnd - 1;
                    orIndex = i;
                }
            }

            if (i + 3 < query.length()){
                if (query.substring(i, i + 3).equals(" | ")){
                    orIndex = i + 3;
                    i = i + 2;
                }
            }

            i++;
        }
    }
    //the given strings starts with [ 
    private int FindWhereBracketCloses(String s){
        int counter = 0;
        int lastI = -1;

        for (int i = 0; i < s.length(); i++){
            lastI = i;
            if (s.charAt(i) == '[')
                counter++;
            else if (s.charAt(i) == ']')
                counter--;
            
            if (counter == 0)
                break;
        }

        if (counter > 0)
            return -1;
        return lastI + 1;
    }

    private int NumberOfCInS(String s, char c){ //number of characters in string
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
             if (s.charAt(i) == c) {
                 count++;
            }
        }
        return count;
    }

    ///GET///
    public String Get(String key){
        try{
            return BuildString(trie.Find(key));
        }catch (Exception e){
            return "No such key found";
        }

    }

    private String BuildString(KeyValueNode key){
        String query;
        query = key.key + " -> ";

        if (key.value != null){
            if (key.value.equals("")){
                query = query + "[]";
            }
            else{
                query = query + key.value;
            }
        }
        else{
            query = query + "[";
            for (int i = 0; i < key.keyValues.size(); i++){
                if (i != 0)
                    query = query + " | ";

                query = query + BuildString(key.keyValues.get(i));
            }
            query = query + "]";
        }

        return query;
    }

    ///DELETE///
    public String Delete(String key){
        try{
            trie.Delete(key);
            return "Deleted: " + key;
        }catch (Exception e){
            return "No such key found";
        }

    }

    ///QUERY///
    public String Query(String keyPath){

        String[] difKeys = keyPath.split("\\.");
        String value = "";

        KeyValueNode kV = new KeyValueNode("");

        try{
            kV = trie.Find(difKeys[0]);
        }catch (Exception e){
            return "No such key found";
        }

        if (kV.key.equals("")){
            return "No such key path exists";
        }

        if (difKeys.length == 1){
            value = Get(difKeys[0]);
        }
        else{
            value = SearchSubKeys(kV, difKeys);
        }


        return value;
    }

    private String SearchSubKeys(KeyValueNode kVNode, String[] keys){
        String value = "";

        KeyValueNode kV = kVNode;
        int keyNumber = 1;
        boolean foundKey = false;

        while (keyNumber < keys.length){

            if (kV.keyValues.size() < 0)
                return "No such key exists";
            
            for (int i = 0; i < kV.keyValues.size(); i++ ){
                if (kV.keyValues.get(i).key.equals(keys[keyNumber])){
                    kV = kV.keyValues.get(i);
                    if (kV.keyValues.size() > 0)
                        value = BuildString(kV);
                    else
                        value = kV.value;

                    foundKey = true;
                    break;
                }
            }

            if (!foundKey)
                return "No such key exists";

            foundKey = false;
            keyNumber++;
        }

        return value;
    }

    ///COMPUTE///
    public String Compute(String mathExp, String queriesString){

        String[] queries = queriesString.split(" AND ");
        for (String q : queries){
            System.out.println(q);
        }
        String[][] qMatrix = new String[queries.length][2];

        for (int i = 0; i < queries.length; i++){
            try{ //the left col contains the name of the variable and the right the value of the variable
                qMatrix[i][0] = queries[i].split("=")[0].trim();
                qMatrix[i][1] = queries[i].split("=")[1].trim();
                qMatrix[i][1] = qMatrix[i][1].split(" ")[1];
                qMatrix[i][1] = Query(qMatrix[i][1]);
            } catch (Exception e){
                System.out.println("Error: " + qMatrix[i][0] + " = " + qMatrix[i][1]);
            }

            //System.out.println("queries:" + qMatrix[i][0] + " = " + qMatrix[i][1]);
            
            if (qMatrix[i][1].contains("\"") || qMatrix[i][1].contains(" ") ||qMatrix[i][1].length() == 0)
                return "One of the queries does not return int or float";
        }
        mathExp = mathExp.replaceAll("\\s", ""); //clear whitespaces

        //replace the name of the variable with its value
        for (int i = 0; i < qMatrix.length; i++){
            mathExp = mathExp.replaceAll(qMatrix[i][0], qMatrix[i][1]);
        }

        System.out.println("mathExp = " + mathExp);

        ComputeMathExpression calculator = new ComputeMathExpression();
        return calculator.Compute(mathExp);
    }

}