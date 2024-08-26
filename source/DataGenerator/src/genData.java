import java.util.List;
import java.util.Random;

public class genData
{

    static int n;
    static int d;
    static int l;
    static int m;

    static Random rand = new Random();

    static List<String> keyNameList;

    public static void main(String[] args){

        String inputFile;
        try{
            inputFile = args[0];
            n = Integer.parseInt(args[1]);
            d = Integer.parseInt(args[2]);
            l = Integer.parseInt(args[3]);
            m = Integer.parseInt(args[4]);
        } catch (Exception e){ 
            System.out.println("Wrong arguements!"); 
            return;
        }

        String ouputFile = "outputFile.txt";

        FileInteract fileInter = new FileInteract(inputFile, ouputFile);
        keyNameList = fileInter.ReadFile();

        fileInter.CreateOutputFile();
        fileInter.WriteFile("", false); //clear outputfile

        String[] queries = new String[n];

        for (int i = 0; i < n; i++){

            int randD = rand.nextInt(d + 1);
            int randM = rand.nextInt(m + 1);

            KeyValueData outerKey = new KeyValueData();

            outerKey.key = "key" + i;
            queries[i] = "\"" + outerKey.key + "\"" + " -> [";

            if (randM == 0){
                queries[i] = queries[i] + "]";
            }
            else {
                queries[i] = FillQuery(outerKey, randD, randM, queries[i]);
            }

            
            fileInter.WriteFile(queries[i] + "\n", true);
            System.out.println(queries[i]);
        }
    }

    static String FillQuery(KeyValueData kv, int de, int keysNumber, String query){
        String q = query;

        int depth = de;
        int newM;

        int kVChild = rand.nextInt(keysNumber); //the key that will have a k-v as a value  
                                                //so that we can reach the desired depth
        for (int j = 0; j < keysNumber; j++){
            KeyValueData newKV = new KeyValueData();
            String randomLine = keyNameList.get(rand.nextInt(keyNameList.size() - 1));
            String keyName = randomLine.split(" ", 2)[0];
            String valueType = randomLine.split(" ", 2)[1];
            //newKV.key = kv.key + "/" + j;
            newKV.key = keyName;
            kv.values.add(newKV);
            q = q + "\"" + kv.values.get(j).key + "\"" + " -> ";
            if (depth > 0 && j == kVChild) {
                q = q + "[";
                newM = rand.nextInt(m + 1) + 1;
                q = FillQuery(kv.values.get(j), depth - 1, newM, q);
            }
            else {
                q = q + RandomValue(valueType);
            }

            if (j + 1 < keysNumber){
                q = q + " | ";
            }
        }
        q = q + "]";
        return q;
    }

    static String RandomValue(String type){

        String s = "";
        float r = rand.nextFloat();

        if (r >= 0.1){
            if (type.toLowerCase().equals("string")){
                s = RandomString();
            }
            else if (type.toLowerCase().equals("float")) {
                float temp = rand.nextFloat() * 1000;
                s = Float.toString(temp);
            }
            else if (type.toLowerCase().equals("int")){
                int temp = rand.nextInt(1000);
                s = Integer.toString(temp); 
            }
        }
        else {
            s = "[]";
        }

        return s;
    }

    static String RandomString(){
        String s = "";
        int randLen = rand.nextInt(l) + 1;
        float r;
        for(int j = 0; j < randLen; j++)
        {
            r = rand.nextFloat();
            if (r <= 0.7){
                if (r < 0.35){
                    s = s + (char)('a' + rand.nextInt(26));
                }
                else{
                    s = s + (char)('A' + rand.nextInt(26));
                }
            }
            else{
                s = s + rand.nextInt(9);
            }
            
        }
        s = "\"" + s + "\"";
        return s;
    }
}
