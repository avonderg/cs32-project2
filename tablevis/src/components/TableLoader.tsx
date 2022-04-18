import React from "react";
import { useState } from "react";
import axios from "axios";
// @ts-ignore
// import Select from "react-select";
// @ts-ignore
import { AwesomeButton } from "react-awesome-button";
// @ts-ignore
import "react-awesome-button/dist/styles.css";

interface TableLoaderInput {
  tableNames: string[];
  change: React.Dispatch<string>;
}

function TableLoader(props: TableLoaderInput) {
  // need to use axios to get the table names
  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    event.preventDefault();
    props.change(event.target.value);
  };
  return (
    // <Select options={options} />
    <div>
      <div className="tableLoaderDiv">
        <div>
          <select onChange={handleChange}>
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
