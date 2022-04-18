import React from "react";
import { useState } from "react";
import axios from "axios";
// @ts-ignore
import { AwesomeButton } from "react-awesome-button";
// @ts-ignore
import "react-awesome-button/dist/styles.css";

// interface containing information that is the input to the 
interface TableLoaderInput {
  tableNames: string[];
  change: React.Dispatch<string>;
}

/**
 * creates the dropdown that allows users to choose a table.
 * @param props a TableLoaderInput containing the info needed to populate the 
 * selection options
 * @returns a dropdown that lets one choose a table to load
 */
function TableLoader(props: TableLoaderInput) {
  /**
   * handles the event that an option is changed in the select menu
   * @param event the change event that we are handling
   */
  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    event.preventDefault();
    props.change(event.target.value);
  };
  return (
    <div>
      <div className="tableLoaderDiv">
        <div>
          <select onChange={handleChange} className="selectTable">
            <option value="" selected disabled hidden>
              Choose here
            </option>
            {props.tableNames.map((x: string) => (
              <option value={x}>{x}</option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
}
export default TableLoader;
