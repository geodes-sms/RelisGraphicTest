import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelisUser {

    private String full_name;
    private String username;
    private String user_email;

    private String password;
    private String user_usergroup;
    private String creation_time;
    private String create_by;
    private String user_state;
    private String user_active;

    @Override
    public boolean equals(Object o){
        RelisUser user = (RelisUser) o;
        if(user != null){

            return user.username.equals(this.username)
                    && user.password.equals(this.password)
                    && user.user_email.equals(this.user_email);
        }
        return false;
    }

    @Override
    public String toString(){

        return "fullName: " + full_name +" , user_name: " + username
                +" user_email " + user_email;

    }

    public String getRelisUserDBFormatSQL(){


        String encriptedPassword = Utility.encriptWiht_MD5(password);
        return String.format("(\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",%s,%s,%s)", full_name,username,user_email,
                user_usergroup,encriptedPassword,creation_time,create_by,user_state,user_active);

    }

}

