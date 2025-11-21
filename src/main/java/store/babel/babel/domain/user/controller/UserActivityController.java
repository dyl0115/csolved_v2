package store.babel.babel.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.bookmark.service.BookmarkService;
import store.babel.babel.domain.post.dto.PostCard;
import store.babel.babel.domain.post.service.PostService;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.global.utils.page.PageInfo;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@RequestMapping("/user/activity")
@RequiredArgsConstructor
@Controller
public class UserActivityController
{
    public static final String VIEWS_USER_ACTIVITY = "/views/user/activity";
    public static final String FRAGMENT_REPLIED_POST_LIST = "/views/user/activity :: repliedPostList";
    public static final String FRAGMENT_USER_POST_LIST = "/views/user/activity :: userPostList";
    private static final String FRAGMENT_BOOKMARK_LIST = "/views/user/activity :: bookmarkList";

    private final BookmarkService bookmarkService;
    private final PostService postService;

    @LoginRequest
    @GetMapping
    public String getUserActivities(@LoginUser User user,
                                    @PageInfo(type = "bookmarkPage") Long bookmarkPage,
                                    @PageInfo(type = "repliedPage") Long repliedPage,
                                    @PageInfo(type = "myPostPage") Long myPostPage,
                                    Model model)
    {
        Long bookmarkCount = bookmarkService.countBookmarks(user.getId());
        Pagination pagination = Pagination.from(bookmarkPage, bookmarkCount);
        List<PostCard> bookmarks = postService.getBookmarkedPostCards(user.getId(), pagination);

        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("bookmarkPagination", pagination);

        Long repliedCount = postService.countAnsweredPosts(user.getId());
        pagination = Pagination.from(repliedPage, repliedCount);
        System.out.println("offset=" + pagination.getOffset() + " size=" + pagination.getSize());
        List<PostCard> replied = postService.getAnsweredPostCards(user.getId(), pagination);

        model.addAttribute("replied", replied);
        model.addAttribute("repliedPagination", pagination);


        Long myPostCount = postService.countMyPosts(user.getId());
        pagination = Pagination.from(myPostPage, myPostCount);
        List<PostCard> myPosts = postService.getMyPosts(user.getId(), pagination);

        model.addAttribute("myPosts", myPosts);
        model.addAttribute("myPostPagination", pagination);

        return VIEWS_USER_ACTIVITY;
    }

    @LoginRequest
    @GetMapping("/bookmark")
    public String getBookmarksAndPage(@LoginUser User user,
                                      @PageInfo(type = "bookmarkPage") Long pageNumber,
                                      Model model)
    {
        Long bookmarkCount = bookmarkService.countBookmarks(user.getId());
        Pagination pagination = Pagination.from(pageNumber, bookmarkCount);
        List<PostCard> bookmarks = postService.getBookmarkedPostCards(user.getId(), pagination);

        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("bookmarkPagination", pagination);

        return FRAGMENT_BOOKMARK_LIST;
    }

    @LoginRequest
    @GetMapping("/myPost")
    public String getUserPosts(@LoginUser User user,
                               @PageInfo(type = "myPostPage") Long myPostPage,
                               Model model)
    {
        Long myPostCount = postService.countMyPosts(user.getId());
        Pagination pagination = Pagination.from(myPostPage, myPostCount);
        List<PostCard> myPosts = postService.getMyPosts(user.getId(), pagination);

        model.addAttribute("myPosts", myPosts);
        model.addAttribute("myPostPagination", pagination);

        return FRAGMENT_USER_POST_LIST;
    }

    @LoginRequest
    @GetMapping("/replied")
    public String getRepliedPost(@LoginUser User user,
                                 @PageInfo(type = "repliedPage") Long repliedPage,
                                 Model model)
    {
        Long repliedCount = postService.countAnsweredPosts(user.getId());
        Pagination pagination = Pagination.from(repliedPage, repliedCount);
        System.out.println("pagination=" + pagination.toString());
        System.out.println("repliedCount=" + repliedCount);
        System.out.println("offset=" + pagination.getOffset() + " size=" + pagination.getSize());

        List<PostCard> replied = postService.getAnsweredPostCards(user.getId(), pagination);
        System.out.println("realSize = " + replied.size());

        model.addAttribute("replied", replied);
        model.addAttribute("repliedPagination", pagination);

        return FRAGMENT_REPLIED_POST_LIST;
    }
}