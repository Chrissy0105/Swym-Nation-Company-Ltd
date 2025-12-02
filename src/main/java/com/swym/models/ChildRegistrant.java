package com.swym.models;

public class ChildRegistrant extends Registrant {
    private String parentName;
    private int age;
    private String behaviorNotes;
    private String illnesses;

    public ChildRegistrant(String name, String parentName, int age, String behaviorNotes, String illnesses, String email) {
        super(name, null, email); // children may not have contactNumber
        this.parentName = parentName;
        this.age = age;
        this.behaviorNotes = behaviorNotes;
        this.illnesses = illnesses;
    }

    public String getParentName()
     { return parentName; }

    public int getAge()
     { return age; }

    public String getBehaviorNotes()
     { return behaviorNotes; }
     
    public String getIllnesses() 
    { return illnesses; }
}
