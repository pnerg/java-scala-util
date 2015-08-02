/**
 *  Copyright 2015 Peter Nerg
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * Contains various utility classes translated from the Scala language. <br>
 * The following three types are all what is known as container types.<br>
 * They represent various use cases, such as containing one or zero value ({@link javascalautils.Option}), one of two values ({@link javascalautils.Either}) and value or exception ({@link javascalautils.Try})
 * The aim with these utility classes is to provide neat and concise programming patterns. <br>
 * <ul>
 *     <li>{@link javascalautils.Option}/{@link javascalautils.Some}/{@link javascalautils.None}</li>
 *     <li>{@link javascalautils.Try}/{@link javascalautils.Success}/{@link javascalautils.Failure}</li>
 *     <li>{@link javascalautils.Either}/{@link javascalautils.Left}/{@link javascalautils.Right}</li>
 * </ul>
 * It is possible to make transitions between the container types. <br>
 * This image below aims to illustrate the transitions that can be made and where they lead. <br>
 * <br>
 * <img src="../doc-files/transitions.png" alt="transitions"><br>
 * <br>
 * Refer to the Javadoc for the classes or the Wiki for more details and examples:<br>
 * <a href="https://github.com/pnerg/java-scala-util">https://github.com/pnerg/java-scala-util</a><br>
 * @author Peter Nerg
 */
package javascalautils;