package org.keycloak.dashboard.rep;

import org.keycloak.dashboard.util.DateUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RetriedPR {

    public static void main(String[] args) throws IOException, ParseException {
        List<RetriedPR> list = RetriedPR.load();
        for (RetriedPR l : list) {
            System.out.println(l.getDate());
        }
    }

    private Date date;
    private Integer prNumber;
    private String runId;
    private Integer attempt;

    public RetriedPR(Date date, Integer prNumber, String runId, Integer attempt) {
        this.date = date;
        this.prNumber = prNumber;
        this.runId = runId;
        this.attempt = attempt;
    }

    public static List<RetriedPR> load() throws IOException, ParseException {
        List<RetriedPR> list = new LinkedList<>();

        File retriedPRsFile = new File("retried-prs");
        List<String> lines = Files.readAllLines(retriedPRsFile.toPath());
        Iterator<String> itr = lines.iterator();
        itr.next();

        while (itr.hasNext()) {
            String[] split = itr.next().split(",");
            Date date = DateUtil.fromJson(split[0].substring(1, split[0].length() - 1));
            Integer prNumber = !split[1].equals("null") ? Integer.parseInt(split[1]) : null;
            String runId = split[2];
            Integer attempt = Integer.parseInt(split[3]);

            RetriedPR retriedPR = new RetriedPR(date, prNumber, runId, attempt);
            list.add(retriedPR);
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
}
