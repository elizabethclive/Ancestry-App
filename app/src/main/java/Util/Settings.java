package Util;

public class Settings {
    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fathersSide;
    private boolean mothersSide;
    private boolean maleEvents;
    private boolean femaleEvents;

    public Settings() {
        this.lifeStoryLines = false;
        this.familyTreeLines = false;
        this.spouseLines = false;
        this.fathersSide = false;
        this.mothersSide = false;
        this.maleEvents = false;
        this.femaleEvents = false;
    }

    public boolean showLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean showFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean showSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean filterFathersSide() {
        return fathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        this.fathersSide = fathersSide;
    }

    public boolean filterMothersSide() {
        return mothersSide;
    }

    public void setMothersSide(boolean mothersSide) {
        this.mothersSide = mothersSide;
    }

    public boolean filterMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean filterFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    // storyLinesColor, treeLinesColor

}
