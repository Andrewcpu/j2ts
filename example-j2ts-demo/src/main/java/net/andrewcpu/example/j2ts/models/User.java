package net.andrewcpu.example.j2ts.models;

import net.andrewcpu.j2ts.annotations.Model;
import net.andrewcpu.j2ts.annotations.NullableField;
import net.andrewcpu.j2ts.annotations.OptionalField;

@Model
public class User {
    public String userId;
    public String name;
    public String email;

    @OptionalField
    public String address;

    @NullableField
    public String username;
}
