package me.raulbalanza.tjv.school_enrollment.client.dto;

public class FilterDto {

    public Integer min_free_capacity;
    public Integer max_credits;
    public boolean error;

    public FilterDto(){
        min_free_capacity = 0;
        max_credits = -1;
    }

    public int getMin_free_capacity() {
        return min_free_capacity;
    }

    public void setMin_free_capacity(int min_free_capacity) {
        this.min_free_capacity = min_free_capacity;
    }

    public int getMax_credits() {
        return max_credits;
    }

    public void setMax_credits(int max_credits) {
        this.max_credits = max_credits;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
