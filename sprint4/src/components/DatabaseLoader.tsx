import React from "react";

// interface containing information that is the input to the databaseloader
interface DatabaseLoaderInput {
  dbNames: string[];
  change: React.Dispatch<string>;
}

/**
 * creates the dropdown that allows users to choose a database.
 * @param props a DatabaseLoaderInput containing the info needed to populate the 
 * selection options
 * @returns a dropdown that lets one choose a database to load
 */
function DatabaseLoader(props: DatabaseLoaderInput) {
  /**
   * handles the event that an option is changed in the select menu
   * @param event the change event that we are handling
   */
  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    event.preventDefault();
    console.log("chaning database");
    props.change(event.target.value);
  };
  return (
    <div>
      <div className="tableLoaderDiv">
        <div>
          <select onChange={handleChange} className="selectDB">
            <option value="" selected disabled hidden>
              Choose here
            </option>
            {props.dbNames.map((x: string) => (
              <option value={x}>{x}</option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
}
export default DatabaseLoader;