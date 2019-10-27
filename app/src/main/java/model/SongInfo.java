package model;

public class SongInfo {
    private String songTittle;
    private int checked;
    private int songFile;


    public SongInfo() {

    }



    public SongInfo(String songTittle, int checked, int songFile) {
        this.songTittle = songTittle;
        this.checked = checked;
        this.songFile = songFile;
    }

    public SongInfo(String songTittle, int checked) {
        this.songTittle = songTittle;
        this.checked = checked;
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

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getSongFile() {
        return songFile;
    }

    public void setSongFile(int songFile) {
        this.songFile = songFile;
    }
}
