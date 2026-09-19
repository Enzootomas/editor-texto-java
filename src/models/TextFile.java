package models;

public class TextFile {

    private String name;
    private String content;
    private int size;

    public TextFile(String name) {
        this(name, "");
    }

    public TextFile(String name, String content) {
        this.name = checkName(name);
        this.content = content == null ? "" : content;
        this.size = calculeSize();
    }

    public void edit(String newContent) {
        this.content = newContent == null ? "" : newContent;
        this.size = calculeSize();
    }

    public boolean rename(String newName) {
        this.name = checkName(newName);
        return true;
    }

    public void clear() {
        this.content = "";
        this.size = calculeSize();
    }

    private String checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "O nome do arquivo não pode ser vazio."
            );
        }

        String checkedName = name
            .trim()
            .replaceAll("[^\\p{L}\\p{N} ]", "")
            .replaceAll("\\s+", " ");

        if (checkedName.isBlank()) {
            throw new IllegalArgumentException(
                "O nome deve possuir letras ou números."
            );
        }

        return checkedName;
    }

    private int calculeSize() {
        return content.length();
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Nome: " + name
            + " | Tamanho: " + size
            + " bytes";
    }
}