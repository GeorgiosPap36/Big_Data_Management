import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileInteract
{
    private String inputPath;
    private String outputPath;

    public FileInteract(String inputPath, String outputPath){
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public List<String> ReadFile()
    {
        List<String> inputs = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            String txtLine = reader.readLine();
            while (txtLine != null) {
                inputs.add(txtLine);
                txtLine = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("Incorrect input file path");
        }
        return inputs;
    }

    public void WriteFile(String text, boolean append)
    {
        try {
            FileWriter outputTxt = new FileWriter(outputPath, append);
            outputTxt.write(text);
            outputTxt.close();
        } catch (IOException ioe) {
            System.out.println("Incorrect output file path");
        }
    }

    public void CreateOutputFile(){
        try {
            File temp = new File(outputPath);
            if (temp.createNewFile()) {
                System.out.println("Created file");
            } else {
                System.out.println("File already exists");
            }
        }
        catch (IOException ioe){
            System.out.println("Incorrect input file path");
        }
    }
}
