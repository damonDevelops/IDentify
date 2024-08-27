package com.team.identify.IdentifyAPI.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ips_stats")
public class IPSStats {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int cpuUtilisation;
    private int ramUtilisation;
    private Long maxRam;
    private Long usedRam;

    private Instant time;

    private int gpu1VramUtilisation;
    private int gpu1Utilisation;
    private int gpu2VramUtilisation;
    private int gpu2Utilisation;
    private int gpu3VramUtilisation;
    private int gpu3Utilisation;
    private int gpu4VramUtilisation;
    private int gpu4Utilisation;
    private int gpu5VramUtilisation;
    private int gpu5Utilisation;
    private int gpu6VramUtilisation;
    private int gpu6Utilisation;
    private int gpu7VramUtilisation;
    private int gpu7Utilisation;
    private int gpu8VramUtilisation;
    private int gpu8Utilisation;


    @ManyToOne
    private IPS relatedIPS;

    public IPSStats(){

    }

    public IPSStats(int cpuUtilisation, int ramUtilisation, Long maxRam, Long usedRam){
        this.cpuUtilisation = cpuUtilisation;
        this.ramUtilisation = ramUtilisation;
        this.maxRam = maxRam;
        this.usedRam = usedRam;
        this.time = Instant.now();
    }

    public int getCpuUtilisation() {return cpuUtilisation;}

    public void setCpuUtilisation(int cpuUtilisation) {this.cpuUtilisation = cpuUtilisation;}

    public int getRamUtilisation() {return ramUtilisation;}

    public void setRamUtilisation(int ramUtilisation) {this.ramUtilisation = ramUtilisation;}

    public Long getMaxRam() {return maxRam;}

    public void setMaxRam(Long maxRam) {this.maxRam = maxRam;}

    public Long getUsedRam() {return usedRam;}

    public void setUsedRam(Long usedRam) {this.usedRam = usedRam;}

    public IPS getRelatedIPS() {return relatedIPS;}

    public void setRelatedIPS(IPS relatedIPS) {this.relatedIPS = relatedIPS;}

    public Instant getTime() {return time;}

    public void setTime(Instant time) {this.time = time;}

    // Setter for all gpu utils, takes int[8], int[8] as arguments
    public void setGPUUtil(int[] VramUtil, int[] Util){
        setGpu1VramUtilisation(VramUtil[0]);
        setGpu1Utilisation(Util[0]);
        setGpu2VramUtilisation(VramUtil[1]);
        setGpu2Utilisation(Util[1]);
        setGpu3VramUtilisation(VramUtil[2]);
        setGpu3Utilisation(Util[2]);
        setGpu4VramUtilisation(VramUtil[3]);
        setGpu4Utilisation(Util[3]);
        setGpu5VramUtilisation(VramUtil[4]);
        setGpu5Utilisation(Util[4]);
        setGpu6VramUtilisation(VramUtil[5]);
        setGpu6Utilisation(Util[5]);
        setGpu7VramUtilisation(VramUtil[6]);
        setGpu7Utilisation(Util[6]);
        setGpu8VramUtilisation(VramUtil[7]);
        setGpu8Utilisation(Util[7]);
    }
    // Getter/Setters for GPU util
    public int getGpu1VramUtilisation() {return gpu1VramUtilisation;}
    public void setGpu1VramUtilisation(int gpu1VramUtilisation) {this.gpu1VramUtilisation = gpu1VramUtilisation;}
    public int getGpu1Utilisation() {return gpu1Utilisation;}
    public void setGpu1Utilisation(int gpu1Utilisation) {this.gpu1Utilisation = gpu1Utilisation;}
    public int getGpu2VramUtilisation() {return gpu2VramUtilisation;}
    public void setGpu2VramUtilisation(int gpu2VramUtilisation) {this.gpu2VramUtilisation = gpu2VramUtilisation;}
    public int getGpu2Utilisation() {return gpu2Utilisation;}
    public void setGpu2Utilisation(int gpu2Utilisation) {this.gpu2Utilisation = gpu2Utilisation;}
    public int getGpu3VramUtilisation() {return gpu3VramUtilisation;}
    public void setGpu3VramUtilisation(int gpu3VramUtilisation) {this.gpu3VramUtilisation = gpu3VramUtilisation;}
    public int getGpu3Utilisation() {return gpu3Utilisation;}
    public void setGpu3Utilisation(int gpu3Utilisation) {this.gpu3Utilisation = gpu3Utilisation;}
    public int getGpu4VramUtilisation() {return gpu4VramUtilisation;}
    public void setGpu4VramUtilisation(int gpu4VramUtilisation) {this.gpu4VramUtilisation = gpu4VramUtilisation;}
    public int getGpu4Utilisation() {return gpu4Utilisation;}
    public void setGpu4Utilisation(int gpu4Utilisation) {this.gpu4Utilisation = gpu4Utilisation;}
    public int getGpu5VramUtilisation() {return gpu5VramUtilisation;}
    public void setGpu5VramUtilisation(int gpu5VramUtilisation) {this.gpu5VramUtilisation = gpu5VramUtilisation;}
    public int getGpu5Utilisation() {return gpu5Utilisation;}
    public void setGpu5Utilisation(int gpu5Utilisation) {this.gpu5Utilisation = gpu5Utilisation;}
    public int getGpu6VramUtilisation() {return gpu6VramUtilisation;}
    public void setGpu6VramUtilisation(int gpu6VramUtilisation) {this.gpu6VramUtilisation = gpu6VramUtilisation;}
    public int getGpu6Utilisation() {return gpu6Utilisation;}
    public void setGpu6Utilisation(int gpu6Utilisation) {this.gpu6Utilisation = gpu6Utilisation;}
    public int getGpu7VramUtilisation() {return gpu7VramUtilisation;}
    public void setGpu7VramUtilisation(int gpu7VramUtilisation) {this.gpu7VramUtilisation = gpu7VramUtilisation;}
    public int getGpu7Utilisation() {return gpu7Utilisation;}
    public void setGpu7Utilisation(int gpu7Utilisation) {this.gpu7Utilisation = gpu7Utilisation;}
    public int getGpu8VramUtilisation() {return gpu8VramUtilisation;}
    public void setGpu8VramUtilisation(int gpu8VramUtilisation) {this.gpu8VramUtilisation = gpu8VramUtilisation;}
    public int getGpu8Utilisation() {return gpu8Utilisation;}
    public void setGpu8Utilisation(int gpu8Utilisation) {this.gpu8Utilisation = gpu8Utilisation;}

}
