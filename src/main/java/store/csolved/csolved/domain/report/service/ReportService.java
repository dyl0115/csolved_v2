package store.csolved.csolved.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.answer.mapper.AnswerMapper;
import store.csolved.csolved.domain.comment.mapper.CommentMapper;
import store.csolved.csolved.domain.post.mapper.PostMapper;
import store.csolved.csolved.domain.report.dto.*;
import store.csolved.csolved.domain.report.mapper.ReportMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService
{
    private final ReportMapper reportMapper;
    private final PostMapper postMapper;
    private final AnswerMapper answerMapper;
    private final CommentMapper commentMapper;

    @Transactional
    public void createReport(ReportCreateCommand command)
    {
        reportMapper.createReport(command);
    }

    @Transactional
    public void approveReports(ReportUpdateCommand command)
    {
        reportMapper.approveReports(command);
        deleteContentByAdmin(command);
    }

    @Transactional
    public void rejectReports(ReportUpdateCommand command)
    {
        reportMapper.rejectReports(command);
    }

    @Transactional
    public void approveRejectedReports(ReportUpdateCommand command)
    {
        reportMapper.approveRejectedReports(command);
        deleteContentByAdmin(command);
    }

    @Transactional
    public void rejectApprovedReports(ReportUpdateCommand command)
    {
        reportMapper.rejectApprovedReports(command);
        restoreContentByAdmin(command);
    }

    @Transactional
    public void resetToPending(ReportUpdateCommand command)
    {
        reportMapper.resetToPending(command);
        restoreContentByAdmin(command);
    }

    @Transactional(readOnly = true)
    public List<ReportCard> getReports(ReportSearchQuery query)
    {
        return reportMapper.getReports(query);
    }

    @Transactional(readOnly = true)
    public String getDetailReason(Long reportId)
    {
        return reportMapper.getDetailReason(reportId);
    }

    @Transactional(readOnly = true)
    public Long countAll()
    {
        return reportMapper.countAll();
    }

    @Transactional(readOnly = true)
    public Long countPending()
    {
        return reportMapper.countPending();
    }

    @Transactional(readOnly = true)
    public Long countRejected()
    {
        return reportMapper.countRejected();
    }

    @Transactional(readOnly = true)
    public Long countApproved()
    {
        return reportMapper.countApproved();
    }

    @Transactional(readOnly = true)
    public Long countReports(ReportCountQuery query)
    {
        return reportMapper.countReports(query);
    }

    private void deleteContentByAdmin(ReportUpdateCommand command)
    {
        switch (command.getTargetType())
        {
            case POST -> postMapper.deletePostByAdmin(command.getTargetId());
            case ANSWER ->
            {
                postMapper.decreaseAnswerCount(answerMapper.getPostId(command.getTargetId()));
                answerMapper.deleteAnswerByAdmin(command.getTargetId());
            }
            case COMMENT ->
            {
                postMapper.decreaseAnswerCount(commentMapper.getPostId(command.getTargetId()));
                commentMapper.deleteCommentByAdmin(command.getTargetId());
            }
        }
    }

    private void restoreContentByAdmin(ReportUpdateCommand command)
    {
        switch (command.getTargetType())
        {
            case POST -> postMapper.restorePostByAdmin(command.getTargetId());
            case ANSWER ->
            {
                postMapper.increaseAnswerCount(answerMapper.getPostId(command.getTargetId()));
                answerMapper.restoreAnswerByAdmin(command.getTargetId());
            }
            case COMMENT ->
            {
                postMapper.increaseAnswerCount(commentMapper.getPostId(command.getTargetId()));
                commentMapper.restoreCommentByAdmin(command.getTargetId());
            }
        }
    }
}
