package afip.cda.projet_crud.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "t_users")
public
class User {
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "mdp";
    @DatabaseField(columnName = "id", generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private String nom;
    @DatabaseField(canBeNull = false)
    private String prenom;
    @DatabaseField(columnName = FIELD_EMAIL)
    private String email;
    @DatabaseField
    private String photo;
    @DatabaseField(canBeNull = false, columnName = FIELD_PASSWORD)
    private String mdp;

    public User() {
    }

    public User(int id, String nom, String prenom, String email, String photo, String mdp) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.photo = photo;
        this.mdp = mdp;
    }

    public User(String nom, String prenom, String email, String photo, String mdp) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.photo = photo;
        this.mdp = mdp;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    @Override
    public String toString() {
        return "id=" + id +", " + nom  +" " + prenom;
    }

    //conversion du profil au format JSONArray
    public JSONArray convertToJSONArray(){
        List list = new ArrayList();
        list.add(nom);
        list.add(prenom);
        list.add(email);
        list.add(photo);
        list.add(mdp);
        return new JSONArray(list);
    }
}
