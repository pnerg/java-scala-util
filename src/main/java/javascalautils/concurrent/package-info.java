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
 * Contains utilities for concurrent/asynchronous programming. <br>
 * The core principle is the {@link javascalautils.concurrent.Promise}/{@link javascalautils.concurrent.Future} pattern which is the way to separate the execution/job side ({@link javascalautils.concurrent.Promise}) from the client/receiver side ({@link javascalautils.concurrent.Future}). <br>
 * 
 * <h2>Promise/Future</h2>
 * In an essence the {@link javascalautils.concurrent.Promise} is the promise to deliver a value at some time in the future.<br>
 * This is the handle the actual computation side of of the job uses. <br>
 * <br>
 * The {@link javascalautils.concurrent.Future} is the bearer of a value-to-be, an execution yet to be finished. <br>
 * The purpose is to have a placeholder for a value from an asynchronous computation. <br>
 * The preferred way to get hold of the value-to-be is to register a listener on any of the provided listener types since this allows for asynchronous
 * non-blocking operations. <br>
 * The following image outlines an example of using a {@link javascalautils.concurrent.Promise}/{@link javascalautils.concurrent.Future} pair.<br> 
 * <br> 
 * <img src="./doc-files/flowchart-future.png" alt="flowchart"><br>
 * <br>
 * <h2>Executing a function</h2>
 * In addition to manually using the {@link javascalautils.concurrent.Executor} class for performing asynchronous computations one can let the {@link javascalautils.concurrent.Future} class deal with that automatically. <br>
 * This is done by using {@link javascalautils.concurrent.Future#apply(javascalautils.ThrowableFunction0) Future.apply} or statically importing the {@link javascalautils.concurrent.FutureCompanion#Future(javascalautils.ThrowableFunction0) FutureCompanion.Future} method from the companion class to the {@link javascalautils.concurrent.Future}. <br>
 * <blockquote>
 * 
 * <pre>
 * import static javascalautils.concurrent.FutureCompanion.Future;
 * 
 * Future&lt;Integer&gt; resultSuccess = Future(() -&gt; 9 / 3); // The Future will at some point contain: Success(3)
 * Future&lt;Integer&gt; resultFailure = Future(() -&gt; 9 / 0); // The Future will at some point contain: Failure(ArithmeticException)
 * </pre>
 * 
 * </blockquote>
 * The execution path for the above example is illustrated in the picture below.<br>
 * <br>
 * <img src="./doc-files/flowchart-future-autoexecute.png" alt="flowchart"><br>
 * <br>
 * Refer to the Javadoc for the classes or the Wiki for more details and examples:<br>
 * <a href="https://github.com/pnerg/java-scala-util">https://github.com/pnerg/java-scala-util</a><br>
 * @author Peter Nerg
 *
 */
package javascalautils.concurrent;