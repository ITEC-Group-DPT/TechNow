package com.example.projectlogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ManageUser extends AppCompatActivity {
    private static final String FILE_NAME = "UsernamePassword.txt";
    public static final String SHARED_PREFS = "rememberMe";
    private static ArrayList<User> userList;

    public ManageUser() {
    }

    public User readFileToUser(String account) {
        String[] userInfo = account.split("∙");
        String name = userInfo[0];
        String password = userInfo[1];

        User user = new User(name, password);
        return user;
    }

    public ManageUser(Context context) {
        FileInputStream fis = null;
        userList = new ArrayList<>();
        try {
            fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                String account = line;
                User user = readFileToUser(account);
                userList.add(user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checkSignUpUser(User user, Context context) {
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            Toast.makeText(context, "Username or password can't be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < userList.size(); i++) {
            if (user.getUsername().equals(userList.get(i).getUsername())) {
                Toast.makeText(context, "There's already a user with the username '" + user.getUsername() + "'", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public boolean signUpUser(User user, Context context) {
        FileOutputStream fos = null;
        if (checkSignUpUser(user, context) == true) {
            try {
                fos = context.openFileOutput(FILE_NAME, context.MODE_APPEND);
                fos.write((user.getUsername() + '∙' + user.getPassword() + "\n").getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } else return false;
    }

    public boolean checkLoginUser(User user) {
        for (int i = 0; i < userList.size(); i++) {
            if (user.getUsername().equals(userList.get(i).getUsername()) && user.getPassword().equals(userList.get(i).getPassword())) {
                return true;
            }
        }
        return false;
    }

    public String arrayToString(ArrayList<User> UserList) {
        String usersStr = "";
        for (int i = 0; i < UserList.size(); i++) {
            usersStr = usersStr + UserList.get(i).getUsername() + '∙' + UserList.get(i).getPassword() + "\n";
        }
        return usersStr;
    }

    public void changePasswordFromArray(String username, String newPassword) {
        for (int i = 0; i < userList.size(); i++) {
            if (username.equals(userList.get(i).getUsername())) {
                userList.get(i).setPassword(newPassword);
            }
        }
    }

    public void changePassword(String username, String newPassword, Context context) {
        if (newPassword.equals("")) {
            Toast.makeText(context, "Password can't be blank", Toast.LENGTH_SHORT).show();
        } else {
            FileOutputStream fos = null;
            changePasswordFromArray(username, newPassword);
            String usersStr = arrayToString(userList);

            try {
                fos = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
                fos.write(usersStr.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Toast.makeText(context, "Your password has been updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeUserFromArray(String username) {
        for (int i = 0; i < userList.size(); i++) {
            if (username.equals(userList.get(i).getUsername())) {
                userList.remove(i);
            }
        }
    }

    public void deleteUser(String username, Context context) {
        FileOutputStream fos = null;
        removeUserFromArray(username);
        String usersStr = arrayToString(userList);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(username);
        editor.commit();

        try {
            fos = context.openFileOutput(FILE_NAME, context.MODE_PRIVATE);
            fos.write(usersStr.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getUsersList(Context context) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
