package Backend;

/**
 * Here the configuration for the simulation core is done
 */
class BackendSettings {
    /**
     * Set this variable to "true" if parallel simulation is needed
     */
    static final boolean PARALLEL_MODE_ENABLED = false;

    /**
     * If PARALLEL_MODE_ENABLED is set to "true", this variable
     * should contain the number of threads used for running the jobs
     */
    static final int NUM_THREADS = 2;

    /**
     * Set this variable to "true" if real-time visualisations are
     * needed (using LiveGraph)
     */
    static final boolean VISUALISATION_ENABLED = true;
}
