/**
 * Classes in this package orchestrate the validation process.
 * The validation process relies on a master objects' list to match those DB objects in the request.
 * The objective is to verify whether those entries in the XLSX file are accurate.
 * When an object is not found in the master list, a few options are explored and when feasible, a suggestion is provided.
 * @author danielgalassi@gmail.com
 *
 */
package org.validator.engine;
