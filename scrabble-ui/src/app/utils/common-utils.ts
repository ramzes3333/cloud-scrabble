export function waitForCondition(conditionFunction: () => boolean, timeout: number, interval: number): Promise<void> {
  return new Promise((resolve, reject) => {
    const startTime = Date.now();

    function checkCondition() {
      if (conditionFunction()) {
        resolve();
      } else if (Date.now() - startTime > timeout) {
        reject(new Error("Timeout"));
      } else {
        setTimeout(checkCondition, interval);
      }
    }

    checkCondition();
  });
}
