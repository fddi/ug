const StringUtils = {
  isEmpty: function (str) {
    if (str === null || str === undefined || str === "" || str === "null" || str === "undefined" || JSON.stringify(str) == "{}")
      return true;
    if (str.length === 0)
      return true;
    return false;
  },
  clear: function (str) {
    if (this.isEmpty(str)) {
      return "";
    }
    return str;
  },
}

export default StringUtils;