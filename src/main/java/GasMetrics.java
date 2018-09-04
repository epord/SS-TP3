public enum GasMetrics {
    DY_PRESSURE, //The temporary pressure as the system stabilises
    P_DIFF, //The standard deviation of pressure between the walls of the simulation
    BALANCE, //The percentage of particles in the right side
    EQ_TEMPERATURE, //The end temperature of the system
    EQ_TIME, //The time when the system is balanced
    EQ_PRESSURE, //The pressure after the system has been in equilibrium for a while
    FINAL_PRESSURES, //The final pressure, to graph Pressure/Temperature
    FINAL_TEMPERATURE, //The final temperature, to graph Pressure/Temperature
    FINAL_TP_RATIO, //The ratio TEMPERATURE/PRESSURE in equilibrium
}
