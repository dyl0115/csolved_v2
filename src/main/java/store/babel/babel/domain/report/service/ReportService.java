package store.babel.babel.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.answer.mapper.AnswerMapper;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.post.mapper.PostMapper;
import store.babel.babel.domain.report.dto.*;
import store.babel.babel.domain.report.mapper.ReportMapper;

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
    public void updateReports(ReportUpdateCommand command)
    {
        switch (command.getStatus())
        {
            case RESOLVED ->
            {
                reportMapper.resolveReports(command);
                reportMapper.reprocessReports(command);
                deleteContent(command);
            }
            case REJECTED ->
            {
                reportMapper.rejectReports(command);
            }
        }
    }

    @Transactional
    public void reprocessReports(ReportUpdateCommand command)
    {
        reportMapper.reprocessReports(command);
        postMapper.deletePost(command.getTargetId());
    }

    @Transactional
    public void undoReportActions(ReportUpdateCommand command)
    {
        reportMapper.undoReportActions(command);
        postMapper.restorePost(command.getTargetId());
    }

    public List<ReportCard> getReports(ReportSearchQuery query)
    {
        return reportMapper.getReports(query);
    }

    public Long countAll()
    {
        return reportMapper.countAll();
    }

    public Long countPending()
    {
        return reportMapper.countPending();
    }

    public Long countRejected()
    {
        return reportMapper.countRejected();
    }

    public Long countResolved()
    {
        return reportMapper.countResolved();
    }

    public Long countReports(ReportCountQuery query)
    {
        return reportMapper.countReports(query);
    }

    public String getDetailReason(Long reportId)
    {
        return reportMapper.getDetailReason(reportId);
    }

    private void deleteContent(ReportUpdateCommand command)
    {
        switch (command.getTargetType())
        {
            case POST ->
            {
                postMapper.deletePost(command.getTargetId());
            }
            case ANSWER ->
            {
                answerMapper.deleteAnswer(command.getTargetId());
            }
            case COMMENT ->
            {
                commentMapper.deleteComment(command.getTargetId());
            }
        }
    }
}
