package com.whut.smartinspection.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by think on 2017/11/3.
 */
public class ResultObject {
    private int error;
    private String status;
    private String date;
    private Result[] results;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Result[] getResults() {
        return results;
    }

    public void setResults(Result[] results) {
        this.results = results;
    }

    public static class Result {
        private String currentCity;
        private String pm25;
        private IndexObject[] index;
        @SerializedName("weather_data")
        private Weather[] weatherData;

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getCurrentCity() {
            return currentCity;
        }

        public void setCurrentCity(String currentCity) {
            this.currentCity = currentCity;
        }

        public IndexObject[] getIndex() {
            return index;
        }

        public void setIndex(IndexObject[] index) {
            this.index = index;
        }

        public Weather[] getWeatherData() {
            return weatherData;
        }

        public void setWeatherData(Weather[] weatherData) {
            this.weatherData = weatherData;
        }
    }

    public static class IndexObject {
        private String title;
        private String zs;
        private String tipt;
        private String des;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getZs() {
            return zs;
        }

        public void setZs(String zs) {
            this.zs = zs;
        }

        public String getTipt() {
            return tipt;
        }

        public void setTipt(String tipt) {
            this.tipt = tipt;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }

    public static class Weather {
        private String date;
        private String dayPictureUrl;
        private String nightPictureUrl;
        private String weather;
        private String wind;
        private String temperature;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDayPictureUrl() {
            return dayPictureUrl;
        }

        public void setDayPictureUrl(String dayPictureUrl) {
            this.dayPictureUrl = dayPictureUrl;
        }

        public String getNightPictureUrl() {
            return nightPictureUrl;
        }

        public void setNightPictureUrl(String nightPictureUrl) {
            this.nightPictureUrl = nightPictureUrl;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }
    }
}
