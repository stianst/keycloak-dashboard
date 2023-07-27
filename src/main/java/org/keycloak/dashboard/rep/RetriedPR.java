package org.keycloak.dashboard.rep;

import com.opencsv.CSVReader;
import org.keycloak.dashboard.ci.ResolvedIssue;
import org.keycloak.dashboard.ci.ResolvedIssues;
import org.keycloak.dashboard.util.DateUtil;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RetriedPR {

    private Date date;
    private Integer prNumber;
    private String runId;
    private Integer attempt;

    private ResolvedIssue resolvedBy;

    private String[] failedJob;

    public RetriedPR(Date date, Integer prNumber, String runId, Integer attempt, String[] failedJob) {
        this.date = date;
        this.prNumber = prNumber;
        this.runId = runId;
        this.attempt = attempt;
        this.failedJob = failedJob;
    }

    public static List<RetriedPR> load(ResolvedIssues resolvedIssues) throws IOException, ParseException {
        List<RetriedPR> list = new LinkedList<>();

        CSVReader reader = new CSVReader(new FileReader("retried-prs"));
        Iterator<String[]> itr = reader.iterator();
        itr.next();

        while (itr.hasNext()) {
            String[] split = itr.next();
            Date date = DateUtil.fromJson(split[0]);
            Integer prNumber = !split[1].equals("null") ? Integer.parseInt(split[1]) : null;
            String runId = split[2];
            Integer attempt = Integer.parseInt(split[3]);
            String conclusion = split[4];
            String[] failedJob = !split[5].equals("null") ? split[5].split("; ") : null;

            if (conclusion.equals("failure")) {
                RetriedPR retriedPR = new RetriedPR(date, prNumber, runId, attempt, failedJob);
                retriedPR.setResolvedBy(resolvedIssues.getResolved(retriedPR));
                if (retriedPR.getResolvedBy() == null || !retriedPR.getResolvedBy().isResolved()) {
                    list.add(retriedPR);
                }
            }
        }
        return list;
    }

    public Date getDate() {
        return date;
    }

    public Integer getPrNumber() {
        return prNumber;
    }

    public String getRunId() {
        return runId;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public ResolvedIssue getResolvedBy() {
        return resolvedBy;
    }

    public String[] getFailedJob() {
        return failedJob;
    }

    public void setResolvedBy(ResolvedIssue resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
}
