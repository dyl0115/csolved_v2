package store.babel.babel;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class TestController
{
    @GetMapping("/test")
    public String getUsers(Model model)
    {
        model.addAttribute("users", getInitialUsers());
        return "/test";
    }

    @GetMapping("/test/update")
    public String updateUsers(Model model)
    {
        model.addAttribute("users", getUpdatedUsers());
        return "/test :: userList";
    }


    private List<User> getInitialUsers()
    {
        return Arrays.asList(
                new User(1L, "김철수", "사원"),
                new User(2L, "이영희", "대리")
        );
    }

    private List<User> getUpdatedUsers()
    {
        return Arrays.asList(
                new User(1L, "김철수", "대리"),  // 직급 변경됨
                new User(2L, "이영희", "과장"),  // 직급 변경됨
                new User(3L, "박지성", "사원")   // 새 사용자
        );
    }
}

@Getter
class User
{
    Long id;
    String name;
    String position;

    public User(Long id, String name, String position)
    {
        this.id = id;
        this.name = name;
        this.position = position;
    }
}
