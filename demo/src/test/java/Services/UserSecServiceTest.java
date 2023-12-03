package Services;

import com.travelbuddy.demo.Entities.UserSecurity;
import com.travelbuddy.demo.Repository.UserSecRepo;
import com.travelbuddy.demo.Services.UserSecService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserSecServiceTest {

    @Mock
    private UserSecRepo userSecRepo;

    private UserSecService userSecService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userSecService = new UserSecService(userSecRepo);
    }

    @Test
    public void testFindByUsername() {
        UserSecurity userSec = new UserSecurity();
        Optional<UserSecurity> optionalUserSec = Optional.of(userSec);

        when(userSecRepo.findByUsername("1")).thenReturn(optionalUserSec);
        Optional<UserSecurity> result = userSecService.findByUsername("1");

        assertEquals(optionalUserSec, result);
    }

    @Test
    public void testSaveUser() {
        UserSecurity userSec = new UserSecurity();
        userSecService.saveUser(userSec);
        verify(userSecRepo, times(1)).save(userSec);
    }

    @Test
    public void testDeleteUserByUsername() {
        UserSecurity userSec = new UserSecurity();
        userSec.setUsername("1");
        userSecService.deleteUserByUsername("1");
        verify(userSecRepo, times(1)).deleteByUsername("1");
    }
}
