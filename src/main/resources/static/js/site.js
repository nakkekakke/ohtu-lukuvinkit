// MAIN FUNCTIONS

// Submits the parent form of given element.
const submitForm = (element) => {
  const form = element.parentNode
  form.submit()
}

// Toggles the visibility of parent class '.commentarea'.
const toggleComments = (element) => {
  const comments = element
    .parentNode
    .querySelectorAll(".commentarea")

  comments.forEach(comment => {
    if (comment.style.display === "block") {
      comment.style.display = "none"
    } else {
      comment.style.display = "block"
    }
  })
}

// Toggles the visibility of tips.
const filterTips = () => {
  const tips = getTips()

  tips.forEach(tip => {
    if (someTagIncludesFilter(tip, getFilter())
      && !(hideReadIsChecked() && tipIsRead(tip))) {
        tip.style.display = "block"
      } else {
        tip.style.display = "none"
      }
  })
}

// Toggles the visibility of form elements based on type.
const newReadingTipFormRefresh = () => {
  const typeField = document.getElementById("typeField")

  const urlField = document.getElementById("urlField")
  const authorField = document.getElementById("authorField")
  const isbnField = document.getElementById("isbnField")
  
  const url = document.getElementById("urlInput")
  const author = document.getElementById("authorInput")
  const isbn = document.getElementById("isbnInput")

  if (typeField.value == "Link") {
    author.value = "";
    isbn.value = "";
    urlField.style.display = "block";
    authorField.style.display = "none";
    isbnField.style.display = "none";
  } else if (typeField.value == "Article") {
    url.value = "";
    isbn.value = "";
    urlField.style.display = "none";
    authorField.style.display = "block";
    isbnField.style.display = "none";
  } else if (typeField.value == "Book") {
    url.value = "";
    urlField.style.display = "none";
    authorField.style.display = "block";
    isbnField.style.display = "block";
  }
}

// HELPER FUNCTIONS

// Checks if tip is read.
const tipIsRead = (tip) => tip.className === "tip-read"

// Check if the 'Hide read' checkbox is checked.
const hideReadIsChecked = () => document.getElementById("filter-read").checked

// Gets all tips in the document.
const getTips = () => document.getElementById("readingTipsList").querySelectorAll("article")

// Gets the value of the search box in the document.
const getFilter = () => convertToSearchString(document.getElementById("search").value)

// Does some string manipulations to be able to compare strings.
const convertToSearchString = _ => _.trim().toUpperCase()

// Returns true if some tag in the tip includes the filter phrase.
const someTagIncludesFilter = (tip, filter) => {
  const tags = [...tip.querySelectorAll(".tag")]

  return tags.some(tag => {
    const tagValue = convertToSearchString(tag.innerText)
    return tagValue.includes(filter)
  })
}