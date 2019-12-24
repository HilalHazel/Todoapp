package com.hilal.todoapp.db;

import java.util.Date;


public class Todo {
    private Long id;
    private String desc;
    private String dueDateStr;
    private Date    dueDate;
    private boolean done;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDueDateStr() {
        return dueDateStr;
    }

    public void setDueDateStr(String dueDateStr) {
        this.dueDateStr = dueDateStr;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
