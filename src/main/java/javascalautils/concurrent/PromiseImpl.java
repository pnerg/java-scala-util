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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import javascalautils.Failure;
import javascalautils.Success;
import javascalautils.Try;

/**
 * The Promise implementation. <br>
 * Provides a reusable implementation for safely publishing results from a Promise into a Future.
 * 
 * @author Peter Nerg
 * @since 1.2
 */
final class PromiseImpl<T> implements Promise<T> {

    private final FutureImpl<T> future = new FutureImpl<>();
    private final AtomicBoolean completed = new AtomicBoolean(false);

    @Override
    public Future<T> future() {
        return future;
    }

    @Override
    public boolean isCompleted() {
        return completed.get();
    }

    @Override
    public void success(T object) {
        complete(new Success<>(object));
    }

    @Override
    public void failure(Throwable throwable) {
        complete(new Failure<>(throwable));
    }

    @Override
    public void complete(Try<T> result) {
        Objects.requireNonNull(result, "Must provide a valid result");
        if (completed.compareAndSet(false, true)) {
            future.complete(result);
        } else {
            throw new IllegalStateException("Attempt to complete an already completed Promise");
        }
    }
}
