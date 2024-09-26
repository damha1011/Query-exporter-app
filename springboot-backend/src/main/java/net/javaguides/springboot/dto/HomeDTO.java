package net.javaguides.springboot.dto;

import net.javaguides.springboot.model.Database;
import net.javaguides.springboot.model.Metric;
import net.javaguides.springboot.model.Queries;

import java.math.BigInteger;
import java.util.List;

public class HomeDTO {
    private List<Database> databases;
    private List<Queries> queries;
    private List<Metric> metrics;

    public HomeDTO(List<Database> databases, List<Queries> queries, List<Metric> metrics) {
        this.databases = databases;
        this.queries = queries;
        this.metrics = metrics;
    }

    public List<Database> getDatabases() {
        return databases;
    }

    public void setDatabases(List<Database> databases) {
        this.databases = databases;
    }

    public List<Queries> getQueries() {
        return queries;
    }

    public void setQueries(List<Queries> queries) {
        this.queries = queries;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }
}
