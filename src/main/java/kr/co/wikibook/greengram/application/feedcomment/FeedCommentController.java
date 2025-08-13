package kr.co.wikibook.greengram.application.feedcomment;

import jakarta.validation.Valid;
import kr.co.wikibook.greengram.application.feedcomment.model.FeedCommentGetReq;
import kr.co.wikibook.greengram.application.feedcomment.model.FeedCommentGetRes;
import kr.co.wikibook.greengram.application.feedcomment.model.FeedCommentPostReq;
import kr.co.wikibook.greengram.config.model.ResultResponse;
import kr.co.wikibook.greengram.config.model.UserPrincipal;
import kr.co.wikibook.greengram.entity.FeedComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/comment")
public class FeedCommentController {
    private final FeedCommentService feedCommentService;

    @PostMapping
    public ResultResponse<Long> postFeedComment(@AuthenticationPrincipal UserPrincipal userPrincipal
                                             , @Valid @RequestBody FeedCommentPostReq req) {
        log.info("signedUserId: {}", userPrincipal.getSignedUserId());
        log.info("req: {}", req);
        long feedCommentId = feedCommentService.postFeedComment(userPrincipal.getSignedUserId(), req);
        return new ResultResponse<>("댓글 등록 완료", feedCommentId);
    }

    @GetMapping
    public ResultResponse<?> getFeedCommentList(@Valid @ModelAttribute FeedCommentGetReq req) {
        log.info("req: {}", req);
        FeedCommentGetRes feedCommentGetRes = feedCommentService.getFeedList(req);
        return new ResultResponse<>(String.format("rows: %d", feedCommentGetRes.getCommentList().size())
                , feedCommentGetRes);
    }
    //삭제시 받아야 할 데이터 feedCommentId + 로그인한 사용자의 PK  (feed_comment_id, signed_user_id)
    //FE - data 전달방식 : Query-String
    @DeleteMapping
    public ResultResponse<?> deleteFeedComment(@AuthenticationPrincipal UserPrincipal userPrincipal
                                            , @RequestParam("feed_comment_id") Long feedCommentId) {
        feedCommentService.deleteFeed(userPrincipal.getSignedUserId(),feedCommentId);
        return new ResultResponse<>("댓글을 삭제하였습니다.", null);
    }


}