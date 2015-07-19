/**
 * Copyright 2015 Peter Nerg
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
package javascalautils.concurrent;

import java.util.concurrent.Executor;

/**
 * Executor for running all runnables in the same calling thread. <br>
 * To be used in environments where a deterministic behavior is needed (e.g. during test).
 * 
 * @author Peter Nerg
 */
class SameThreadExecutor implements Executor {

    /**
     * Invokes {@link Runnable#run()} in the same thread.
     */
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

}
