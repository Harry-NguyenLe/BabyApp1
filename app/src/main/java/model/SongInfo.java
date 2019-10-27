package model;

public class SongInfo {
    private String songTittle;
    private boolean selected;
    private int songFile;


    public SongInfo() {

    }

    public SongInfo(String songTittle, boolean selected, int songFile) {
        this.songTittle = songTittle;
        this.selected = selected;
        this.songFile = songFile;
    }

    public SongInfo(String songTittle, boolean selected) {
        this.songTittle = songTittle;
        this.selected = selected;
    }

    public SongInfo(String songTittle) {
        this.songTittle = songTittle;
    }

    public String getSongTittle() {
        return songTittle;
    }

    public void setSongTittle(String songTittle) {
        this.songTittle = songTittle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getSongFile() {
        return songFile;
    }

    public void setSongFile(int songFile) {
        this.songFile = songFile;
    }
}
