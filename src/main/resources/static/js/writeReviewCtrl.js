bulkAppControllers.controller("writeReviewCtrl", 
		function($scope, $rootScope, $http, $stateParams, $ionicModal) {
	
	$scope.forms = {};
	$scope.reviewCreate = {};
	/*
	$.post("/get-pending-reviews", {id: $rootScope.account.id}, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		console.log(responseObject);
	});*/
	
	$scope.reviews = [
	 {firstName: "bob", 
		lastname: "smith", 
		image: "https://s-media-cache-ak0.pinimg.com/736x/30/6d/aa/306daa2d2a312f8ea44e9e49afc4b9d4.jpg",
		id: 1,
		userId: 1},
		 {firstName: "lucy", 
			lastname: "brown", 
			image: "https://s-media-cache-ak0.pinimg.com/236x/78/fc/e9/78fce942b36fe0deb62564115aab1170.jpg",
			id: 2,
			userId: 1}];
	
	$scope.reviewUser = function(review) {
		$scope.curReview = review;
		$scope.openModal();
		console.log("reviewing!");
	};
	
	$scope.deleteReview = function(ReviewId) {
		console.log("deleting!")
	};
	
	$scope.goProfile = function(id) {
		$state.go('tab.events-profile',{userId: id});
	};
	
	$scope.createReview = function() {
		console.log("creating: " + $scope.reviewCreate);
	}
	
	$ionicModal.fromTemplateUrl('new-review-modal.html', {
    scope : $scope,
    animation : 'slide-in-up'
  }).then(function(modal) {
    $scope.modal = modal;
  });
  $scope.openModal = function() {
    $scope.modal.show();
  };
  $scope.closeModal = function() {
    $scope.modal.hide();
  };
  // Cleanup the modal when we're done with it!
  $scope.$on('$destroy', function() {
    $scope.modal.remove();
  });
  // Execute action on hide modal
  $scope.$on('modal.hidden', function() {
    // Execute action
  });
  // Execute action on remove modal
  $scope.$on('modal.removed', function() {
    // Execute action
  });
	
});