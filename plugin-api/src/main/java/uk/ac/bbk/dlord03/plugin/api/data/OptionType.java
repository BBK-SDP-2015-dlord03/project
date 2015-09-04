package uk.ac.bbk.dlord03.plugin.api.data;

/**
 * 
 * The type of an option contract.
 * 
 * @author David Lord
 * @formatter:off
 */
public enum OptionType {
    /**
     * The option to sell the underlying asset.
     */
    PUT, 
    /**
     * The option to buy the underlying asset.
     */
    CALL
}
