package afip.cda.projet_crud.model;

public class Book {

    private String titre, auteur, publieur, description, isbn, photo, publication, categorie, _thumbnail;
    private int price;
    private boolean _isAvailable;

    public String getPublication() {
        return publication;
    }

    public Book(String titre, String auteur, String description, String isbn, String _thumbnail, String categorie){
        super();
        this.titre = titre;
        this.auteur =auteur;
        this.description = description;
        this.isbn = isbn;
        this._thumbnail = _thumbnail;
        this.categorie = categorie;
    }

    public Book(String isbn, String titre, String auteur, String _thumbnail){
        super();
        this.isbn = isbn;
        this.titre = titre;
        this.auteur =auteur;
        this._thumbnail = _thumbnail;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String get_thumbnail() {
        return _thumbnail;
    }

    public void set_thumbnail(String _thumbnail) {
        this._thumbnail = _thumbnail;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean is_isAvailable() {
        return _isAvailable;
    }

    public void set_isAvailable(boolean _isAvailable) {
        this._isAvailable = _isAvailable;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getPublieur() {
        return publieur;
    }

    public void setPublieur(String publieur) {
        this.publieur = publieur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Book(){

    }
}
