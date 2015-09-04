package com.alcist.anvilcraft.account;

import com.alcist.anvilcraft.account.models.User;
import com.firebase.client.Firebase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Created by istar on 04/09/15.
 */
@RunWith(PowerMockRunner.class)
public class TestAccountFireHelper {

    @Test
    public void testSaveUser() {

        Firebase f = new Firebase("https://anvilcraft.firebaseio.com");
        AccountFireHelper fh = spy(new AccountFireHelper(f));

        User tUser = new User();
        String key = fh.usersRef.push().getKey();
        tUser.minecraftName = "Istar";
        tUser.lastLogin = new Date();
        fh.saveUser(key, tUser);
        verify(fh).saveUser(key, tUser);
    }
}
