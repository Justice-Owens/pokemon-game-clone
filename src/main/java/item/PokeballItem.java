package item;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

 public class PokeballItem extends Item{
    private PokeballType type;
    private String typeName;
    private double catchMultiplier;
    private String description;

    public PokeballItem(PokeballType type){
        this.type = type;
        setup();
    }

    public void setup(){
        JSONParser parser = new JSONParser();
        String imagePath;
        try{
            Object obj = parser.parse(new FileReader("src/main/resources/items/pokeballs/" + type.toString().toLowerCase() + ".json"));
            JSONObject jsonObject = (JSONObject) obj;

            setTypeName((String) jsonObject.get("type"));
            setCatchMultiplier(Double.parseDouble((String) jsonObject.get("catch_modifier")));
            setDescription((String) jsonObject.get("description"));
            imagePath = (String) jsonObject.get("image_path");
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));

        } catch (FileNotFoundException e) {
            System.err.println("File Not Found");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Exception");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Parse Exception");
            System.err.println(e.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //GETTERS AND SETTERS

    public PokeballType getType() {
        return type;
    }

    public void setType(PokeballType type) {
        this.type = type;
    }

    public double getCatchMultiplier() {
        return catchMultiplier;
    }

    public void setCatchMultiplier(double catchMultiplier) {
        this.catchMultiplier = catchMultiplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     public String getTypeName() {
         return typeName;
     }

     public void setTypeName(String typeName) {
         this.typeName = typeName;
     }
 }
