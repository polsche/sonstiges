/**
 * Specifies an interface for SelectItems which can be applied to model classes.
 * Some Components might specifically ask for Select Item, like the TagCloud.
 * Label, Value and Icon must be implemented but its use depends on the component.
 */
export interface ISelectItem {
  // Display Label
  label: string;
  // Associated Value
  value: string;
  // Icon for Display Purposes
  icon: string;
  // should item be clickable
  clickable: boolean;
  // Tooltip
  tooltip: string;
}
