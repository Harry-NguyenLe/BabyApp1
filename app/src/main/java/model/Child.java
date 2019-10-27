package model;

import java.io.Serializable;
import java.util.Arrays;

public class Child implements Serializable {
    private int id;
    private String childName;
    private byte[] childImage;
    private int isChecked;



    public Child() {
    }

    public Child(byte[] childImage) {
        this.childImage = childImage;
    }

    public Child(int id, String childName, byte[] childImage, int isChecked) {
        this.id = id;
        this.childName = childName;
        this.childImage = childImage;
        this.isChecked = isChecked;
    }

    public Child(String childName, byte[] childImage, int isChecked) {
        this.childName = childName;
        this.childImage = childImage;
        this.isChecked = isChecked;
    }


    public Child(int id, String childName, byte[] childImage) {
        this.id = id;
        this.childName = childName;
        this.childImage = childImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Child(String childName, byte[] childImage) {
        this.childName = childName;
        this.childImage = childImage;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public byte[] getChildImage() {
        return childImage;
    }

    public void setChildImage(byte[] childImage) {
        this.childImage = childImage;
    }

    public int getChecked() {
        return isChecked;
    }

    public void setChecked(int checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", childName='" + childName + '\'' +
                ", childImage=" + Arrays.toString(childImage) +
                ", isChecked=" + isChecked +
                '}';
    }
}
